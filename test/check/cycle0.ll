target triple = "x86_64-pc-linux-gnu"

define noundef i32 @f(i32 noundef %0) #0 {
	%i = alloca i32
	store i32 %0, ptr %i
	%sum = alloca i32
	store i32 0, ptr %sum
	br label %check
check:
	%2 = load i32, ptr %i
	%3 = icmp sgt i32 %2, 0
	br i1 %3, label %loop, label %exit
loop:
	%4 = load i32, ptr %sum
	%5 = load i32, ptr %i
	%6 = add i32 %4, %5
	store i32 %6, ptr %sum
	%7 = load i32, ptr %i
	%8 = sub i32 %7, 1
	store i32 %8, ptr %i
	br label %check
	br label %exit
exit:
	%10 = load i32, ptr %sum
	ret i32 %10
}

attributes #0 = { nounwind }