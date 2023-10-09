# Phonetic Model Generator

This is a tool which generates natural language phonetic model 
for [keybr.com](https://www.keybr.com). It takes a text file as 
input and produces either JSON or a compressed binary file as output.

- Produce a compressed binary file:

```bash
./bin/genmodel.sh \
    --alphabet=' abcdefghijklmnopqrstuvwxyz' \
    --order=4 \
    < english.txt
```

- Produce a JSON file:

```bash 
./bin/genmodel.sh \
    --alphabet=' abcdefghijklmnopqrstuvwxyz' \
    --order=2 \
    --json \
    < english.txt
```
