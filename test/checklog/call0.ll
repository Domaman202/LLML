
define noundef i32 @add(i32 noundef %0, i32 noundef %1) #0 {
	%3 = add i32 %0, %1
	ret i32 %3
}

define noundef i32 @f(i32 noundef %0, i32 noundef %1) #0 {
	%3 = call i32 add(i32 %0, i32 %1)
	ret i32 %3
}


attributes #0 = { nounwind }