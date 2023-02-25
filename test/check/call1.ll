target triple = "x86_64-pc-linux-gnu"

define noundef i32 @add(i32 noundef %i, i32 noundef %j) #0 {
	%1 = add i32 %i, %j
	ret i32 %1
}

define noundef i32 @f(i32 noundef %a, i32 noundef %b, i32 noundef %c, i32 noundef %d) #0 {
	%1 = tail call noundef i32 @add(i32 noundef %a, i32 noundef %b)
	%2 = tail call noundef i32 @add(i32 noundef %c, i32 noundef %d)
	%3 = tail call noundef i32 @add(i32 noundef %1, i32 noundef %2)
	ret i32 %3
}

attributes #0 = { nounwind }