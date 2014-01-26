#version 120

uniform sampler2D texture;

uniform vec4 lights[255];
uniform vec2 iResolution;
uniform int count;
uniform float overall_brightness;

void main() {
    gl_FragColor = texture2D(texture, gl_TexCoord[0].xy);
    vec2 pos = gl_FragCoord.xy / iResolution.xy;

    for (int i = 0; i < count; i++) {
        float xdis = abs(pos.x - lights[i].x);
        float ydis = abs(pos.y - lights[i].y);
        float magnitude = sqrt((xdis * xdis) + (ydis * ydis));
        float percent = max((lights[i].w - magnitude) / lights[i].w, 0.0);
        gl_FragColor.rgb *= (overall_brightness * (1.0 - percent)) + (lights[i].z * percent);
    }
}