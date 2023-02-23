
define noundef i32 @add(i32 noundef %0, i32 noundef %1) #0 {
	%3 = add i32 %0, %1
	ret i32 %3
}

define noundef i32 @f(i32 noundef %0, i32 noundef %1, i32 noundef %2, i32 noundef %3) #0 {
	%5 = call noundef i32 @add(i32 noundef %0, i32 noundef %1)
	%6 = call noundef i32 @add(i32 noundef %2, i32 noundef %3)
	%7 = call noundef i32 @add(i32 noundef %5, i32 noundef %6)
	ret i32 %7
}


attributes #0 = { nounwind }