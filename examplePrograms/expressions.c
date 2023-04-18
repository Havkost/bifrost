#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

int a;
int b;
double c;
double d;
bool erLukket;
void rut1();
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
    if (erLukket && erTændt) {
        if (erLukket || erTændt) {
            if (a > b) {
                if (c == d) {
                    if (c == a) {
                        if (erLukket == erTændt) {
                            d = b + c + d + a * c / d;
                        }
                    }
                }
            }
        }
    }
    for(int __i = 0; __i < (a); __i++) {
        rut1();
    }
    for(int __i = 0; __i < (b - 2); __i++) {
        rut1();
    }
    for(int __i = 0; __i < (a + b); __i++) {
        rut1();
    }
    for(int __i = 0; __i < (2 + a); __i++) {
        rut1();
    }
    free_memory();
    return 0;
}

void rut1() {
    printf("%lf\n", a / 12);
    printf("%s\n", b == a? "Sandt" : "Falsk");
}
