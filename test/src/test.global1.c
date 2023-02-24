#include <stdint.h>
#include <assert.h>

extern void set(int32_t);
extern int32_t get();

int main() {
    assert(get() == 33);
    set(14);
    assert(get() == 14);
}