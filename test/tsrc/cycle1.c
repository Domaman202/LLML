#include <stdint.h>
#include <assert.h>

extern int32_t f(int32_t);

int main() {
    assert(f(12) == 78);
}
