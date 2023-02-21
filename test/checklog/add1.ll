define noundef i32 @f(i32 noundef %a, i32 noundef %b) #0 {
	%1 = add i32 %a, %b
	%c = bitcast i32 %1 to i32
	ret i32 %c
}

attributes #0 = { nounwind }