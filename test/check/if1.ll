target triple = "x86_64-pc-linux-gnu"

define noundef i32 @f(i32 noundef %i) #0 {
	%1 = icmp slt i32 %i, 5
	br i1 %1, label %ret5, label %retI
ret5:
	ret i32 5
retI:
	ret i32 %i
}

attributes #0 = { nounwind }