#include <stdio.h>
#include "jni.h"

JNIEXPORT jint JNICALL Java_MyJNI_myTest(JNIEnv *env, jobject obj, jintArray dstArray, jintArray srcArray)
{
    jboolean isCopy = 0;
    jint* csrcArray = (*(*env)->GetIntArrayElements)(env, srcArray, &isCopy);
    jsize dstSize = (*(*env)->GetArrayLength)(env, dstArray);
    jsize srcSize = (*(*env)->GetArrayLength)(env, srcArray);
    jsize length = dstSize >= srcSize? dstSize : srcSize;

    printf("The length is: %u\n", length);
    printf("Is copy available? %d\n", isCopy);
    printf("The sum of source array is: %d", csrcArray[0] + csrcArray[1]);

    jint dstBuffer[32];

    for(jsize i = 0; i < length; i++)
        dstBuffer[i] = csrcArray[i] + i + 100;

    (*(*env)->SetIntArrayRegion)(env, dstArray, 0, length, dstBuffer);

    return 100;
}