declare const AndroidNavigationBar: {
    setLight(light: boolean): Promise<any>;
    setColor(color?: string, duration?: number): Promise<any>;
    setFullscreen(fullscreen?: boolean): Promise<any>;
    setImmersive(immersive?: boolean): Promise<any>;
    show(): Promise<any>;
    hide(): Promise<any>;
};
export default AndroidNavigationBar;
