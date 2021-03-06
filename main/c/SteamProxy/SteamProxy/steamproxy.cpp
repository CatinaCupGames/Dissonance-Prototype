#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <steam_api.h>
#include <com_tog_framework_steam_SteamProxy.h>

jboolean JNICALL Java_com_tog_framework_steam_SteamProxy__1initSteamAPI (JNIEnv *, jobject) {
	bool result = SteamAPI_Init();
	return (jboolean)result;
}

jboolean JNICALL Java_com_tog_framework_steam_SteamProxy__1closeSteamAPI (JNIEnv *, jobject) {
	SteamAPI_Shutdown();

	return (jboolean)true;
}