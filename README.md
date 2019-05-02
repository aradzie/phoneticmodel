# Phonetic Model Generator

This is a tool which generates natural language phonetic model 
for [keybr.com](https://www.keybr.com). It takes a text file as 
input and produces either JSON or compressed binary file as output.

- Produce compressed binary file:

```bash
./bin/genmodel.sh \
    --language=english \
    --order=4 \
    < english.txt
```

- Produce JSON file:

```bash 
./bin/genmodel.sh \
    --language=english \
    --order=2 \
    --json \
    < english.txt
```
