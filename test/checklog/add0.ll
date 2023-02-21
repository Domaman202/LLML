define noundef i32 @f(i32 noundef %a, i32 noundef %b) #0 {
	%1 = add i32 %a, %b
	ret i32 %1
}

attributes #0 = { nounwind }