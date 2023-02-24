target triple = "x86_64-pc-linux-gnu"

@value = external global i32

define void @set(i32 noundef %i) #0 {
	store i32 %i, ptr @value
	ret void
}

define noundef i32 @get() #0 {
	%1 = load i32, ptr @value
	ret i32 %1
}

attributes #0 = { nounwind }