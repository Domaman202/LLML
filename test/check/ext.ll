target triple = "x86_64-pc-linux-gnu"

declare noundef i32 @add(i32 noundef %0, i32 noundef %1) #0 

define noundef i32 @f(i32 noundef %0, i32 noundef %1) #0 {
	%3 = tail call noundef i32 @add(i32 noundef %0, i32 noundef %1)
	ret i32 %3
}


attributes #0 = { nounwind }