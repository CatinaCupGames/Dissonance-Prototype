#version 120

uniform sampler2D texture;

uniform float resolution_width;
uniform float radius;

void main()
{
    vec4 sum = vec4(0.0);
    vec2 tc = gl_TexCoord[0].st;
    float blur = radius / resolution_width;

    sum += texture2D(texture, vec2(tc.x - 4.0 * blur, tc.y)) * 0.05;
    sum += texture2D(texture, vec2(tc.x - 3.0 * blur, tc.y)) * 0.09;
    sum += texture2D(texture, vec2(tc.x - 2.0 * blur, tc.y)) * 0.12;
    sum += texture2D(texture, vec2(tc.x - 1.0 * blur, tc.y)) * 0.15;

    sum += texture2D(texture, vec2(tc.x, tc.y)) * 0.16;

    sum += texture2D(texture, vec2(tc.x + 1.0 * blur, tc.y)) * 0.15;
    sum += texture2D(texture, vec2(tc.x + 2.0 * blur, tc.y)) * 0.12;
    sum += texture2D(texture, vec2(tc.x + 3.0 * blur, tc.y)) * 0.09;
    sum += texture2D(texture, vec2(tc.x + 4.0 * blur, tc.y)) * 0.05;

    gl_FragColor = vec4(sum.rgba);
}