target triple = "x86_64-pc-linux-gnu"

define noundef i32 @add(i32 noundef %a, i32 noundef %b) #0 {
	%1 = add i32 %a, %b
	ret i32 %1
}

define noundef i32 @f(i32 noundef %a, i32 noundef %b) #0 {
	%1 = tail call noundef i32 @add(i32 noundef %a, i32 noundef %b)
	ret i32 %1
}

attributes #0 = { nounwind }