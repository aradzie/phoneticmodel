package com.keybr.phoneticmodel;

import picocli.CommandLine;

final class Main {

    public static void main(String[] args) {
        CommandLine.call(new GeneratePhoneticModel(), args);
    }
}
