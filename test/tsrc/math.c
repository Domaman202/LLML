#include <stdint.h>
#include <assert.h>

extern int32_t abs(int32_t);
extern int64_t labs(int64_t);
extern double fabs(double);
extern long double fabsl(long double);
extern double acos(double);

int main() {
    assert(abs(-12) == 12);
    assert(labs(-21) == 21);
    assert(fabs(-12.21) == 12.21);
    assert(fabsl(-24.02) == 24.02);
    assert((int) (acos(-1) * 1000) == 1745);
}