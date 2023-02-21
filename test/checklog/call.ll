
define noundef i32 @add(i32 noundef %i, i32 noundef %j) #0 {
	%1 = add i32 %i, %j
	ret i32 %1
}

define noundef i32 @f(i32 noundef %a, i32 noundef %b, i32 noundef %c, i32 noundef %d) #0 {
	%1 = call i32 add(i32 %a, i32 %b)
	%2 = call i32 add(i32 %c, i32 %d)
	%3 = call i32 add(i32 %1, i32 %2)
	%4 = sext i32 %3 to i32
	ret i32 %4
}


attributes #0 = { nounwind }