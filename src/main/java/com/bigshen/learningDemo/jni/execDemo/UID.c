#include <jni.h>
#include <sys/types.h>
#include <unistd.h>
#include "UID.h"

JNIEXPORT void JNICALL Java_UID_setuid
  (JNIEnv * env, jclass clazz, jint id) {
  setuid((uid_t)id);
}

JNIEXPORT void JNICALL Java_UID_seteuid
  (JNIEnv * env, jclass clazz, jint id) {
  seteuid((uid_t)id);
}

JNIEXPORT jint JNICALL Java_UID_getuid
  (JNIEnv * env, jclass clazz) {
  return (jint)getuid();
}

JNIEXPORT jint JNICALL Java_UID_geteuid
  (JNIEnv * env, jclass clazz) {
  return (jint)geteuid();
}

JNIEXPORT void JNICALL Java_UID_setgid
  (JNIEnv * env, jclass clazz, jint id) {
  setgid((uid_t)id);
}

JNIEXPORT void JNICALL Java_UID_setegid
  (JNIEnv * env, jclass clazz, jint id) {
  setegid((uid_t)id);
}

JNIEXPORT jint JNICALL Java_UID_getgid
  (JNIEnv * env, jclass clazz) {
  return (jint)getgid();
}

JNIEXPORT jint JNICALL Java_UID_getegid
  (JNIEnv * env, jclass clazz) {
  return (jint)getegid();
}