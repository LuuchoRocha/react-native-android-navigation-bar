import {NativeModules, Platform} from 'react-native';

const NativeAndroidNavigationBar = NativeModules.AndroidNavigationBar;

const AndroidNavigationBar = {
  async setLight(light: boolean): Promise<any> {
    if (Platform.OS === 'android') {
      return NativeAndroidNavigationBar.setLight(light);
    }

    return null;
  },
  async setColor(color: string = '#000000', duration: number = 4000): Promise<any> {
    if (Platform.OS === 'android') {
      return NativeAndroidNavigationBar.setColor(color, duration);
    }

    return null;
  },
  async setFullscreen(fullscreen: boolean = false): Promise<any> {
    if (Platform.OS === 'android') {
      return NativeAndroidNavigationBar.setFullscreen(fullscreen);
    }

    return null;
  },
  async setImmersive(immersive: boolean = false): Promise<any> {
    if (Platform.OS === 'android') {
      return NativeAndroidNavigationBar.setImmersive(immersive);
    }

    return null;
  },
  async show(): Promise<any> {
    if (Platform.OS === 'android') {
      return NativeAndroidNavigationBar.show();
    }

    return null;
  },
  async hide(): Promise<any> {
    if (Platform.OS === 'android') {
      return NativeAndroidNavigationBar.hide();
    }

    return null;
  },
};

export default AndroidNavigationBar;
