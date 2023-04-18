#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

int a;
int b;
double c;
double d;
bool erLukket;
char* tekstTest;
char* x;
bool erTændt;

int free_memory () {
    free(tekstTest);
    free(x);
}

int main() {
    a = 5 + 12;
    b = 5 * a;
    x = malloc(12 * sizeof(char));
    strcpy(x, "Hej med dig");
    erLukket = true;
    erTændt = false;
    c = 30.0;
    d = 40.0;
    tekstTest = malloc(24 * sizeof(char));
    strcpy(tekstTest, "test af Tekst specifier");
    a = b;
    b = a - 5 + 12 * 10;
    printf("%s\n", x);
    printf("%s\n", tekstTest);
    printf("%d\n", a);
    printf("%d\n", b);
    c = d;
    a = b;
    a = 30;
    b = 40;
    tekstTest = "hej";
    tekstTest = "Denne besked er længere end den forrige, allokerer den nu?";
    free_memory();
    return 0;
}

