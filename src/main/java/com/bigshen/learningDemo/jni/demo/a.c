#include <jni.h>

extern int __attribute__((fastcall)) asmTest(void);

JNIEXPORT jint JNICALL Java_test_Main_myJNITest(JNIEnv *env, jobject obj)
{
    return 100 + asmTest();
}