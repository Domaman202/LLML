target triple = "x86_64-pc-linux-gnu"

define noundef i32 @f(i32 noundef %0) #0 {
	%x = alloca i32
	store i32 %0, ptr %x
	%2 = load i32, ptr %x
	%3 = add i32 %2, 1
	store i32 %3, ptr %x
	%4 = load i32, ptr %x
	%5 = add i32 %4, 2
	store i32 %5, ptr %x
	%6 = load i32, ptr %x
	ret i32 %6
}

attributes #0 = { nounwind }