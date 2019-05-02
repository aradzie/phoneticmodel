package com.keybr.phoneticmodel;

import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.concurrent.Callable;

@Command(description = "Generates language phonetic model file.")
public final class GeneratePhoneticModel
        implements Callable<Object> {

    @Option(
            names = {"-l", "--language"},
            required = true,
            converter = LanguageConverter.class,
            description = "Language name."
    )
    private Language language;

    @Option(
            names = {"-o", "--order"},
            required = true,
            description = "Markov chain order. Must be in range from 1 to 4."
    )
    private int order;

    @Option(
            names = {"-j", "--json"},
            description = "Produce JSON instead of binary format."
    )
    private boolean json;

    @Option(
            names = {"-f", "--file"},
            description = "Output JSON or binary file. " +
                    "If not specified, output file name will be derived from language name."
    )
    private Path outputFile;

    @Parameters(
            arity = "0..1",
            description = "Input text file. " +
                    "If not specified, input will be taken from stdin."
    )
    private Path inputFile;

    @Override
    public Object call()
            throws Exception {
        Path out = outputFile;
        if (out == null) {
            if (json) {
                out = Paths.get(String.format("%s-order%d.json", language, order));
            }
            else {
                out = Paths.get(String.format("%s.data", language));
            }
        }
        var input = Normalizer.normalize(getInput(), Normalizer.Form.NFC);
        var table = new TransitionTable(order, language.alphabet());
        table.scan(input);
        if (json) {
            table.writeJson(out);
        }
        else {
            table.writeBinary(out);
        }
        return null;
    }

    private String getInput()
            throws IOException {
        if (inputFile != null) {
            return Files.readString(inputFile);
        }
        else {
            return readFromStdin();
        }
    }

    private static String readFromStdin()
            throws IOException {
        var result = new StringBuilder();
        var reader = new InputStreamReader(System.in);
        while (true) {
            int ch = reader.read();
            if (ch == -1) {
                break;
            }
            result.append((char) ch);
        }
        return result.toString();
    }

    final static class LanguageConverter
            implements ITypeConverter<Language> {

        @Override
        public Language convert(String value)
                throws Exception {
            return Language.from(value);
        }
    }
}
