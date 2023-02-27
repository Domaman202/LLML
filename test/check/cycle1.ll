target triple = "x86_64-pc-linux-gnu"

define noundef i32 @f(i32 noundef %0) #0 {
	%i = alloca i32
	store i32 %0, ptr %i
	%sum = alloca i32
	store i32 0, ptr %sum
	br label %.w0.c
.w0.c:
	%2 = load i32, ptr %i
	%3 = icmp sgt i32 %2, 0
	br i1 %3, label %.w0.l, label %.w0.e
	br label %.w0.l
.w0.l:
	%5 = load i32, ptr %sum
	%6 = load i32, ptr %i
	%7 = add i32 %5, %6
	store i32 %7, ptr %sum
	%8 = load i32, ptr %i
	%9 = sub i32 %8, 1
	store i32 %9, ptr %i
	br label %.w0.c
	br label %.w0.e
.w0.e:
	%11 = load i32, ptr %sum
	ret i32 %11
}

attributes #0 = { nounwind }