target triple = "x86_64-pc-linux-gnu"

define noundef i32 @f(i32 noundef %0, i32 noundef %1) #0 {
	%3 = add i32 %0, %1
	%c = bitcast i32 %3 to i32
	ret i32 %c
}


attributes #0 = { nounwind }