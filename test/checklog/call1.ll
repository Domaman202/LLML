
define noundef i32 @add(i32 noundef %0, i32 noundef %1) #0 {
	%3 = add i32 %0, %1
	ret i32 %3
}

define noundef i32 @f(i32 noundef %0, i32 noundef %1, i32 noundef %2, i32 noundef %3) #0 {
	%5 = call i32 add(i32 %0, i32 %1)
	%6 = call i32 add(i32 %2, i32 %3)
	%7 = call i32 add(i32 %5, i32 %6)
	ret i32 %7
}


attributes #0 = { nounwind }