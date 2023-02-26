#include <stdint.h>
#include <assert.h>

extern float f();

int main() {
    assert(f() == 12.21f);
}