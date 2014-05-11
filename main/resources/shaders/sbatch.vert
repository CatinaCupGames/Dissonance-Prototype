uniform mat4 projection;

attribute vec4 color;
attribute vec2 texcoord;
attribute vec2 position;

varying vec4 varyColor;
varying vec2 varyTexcoord;

void main() {
    varyColor = color;
    varyTexcoord = texcoord;
    gl_Position = projection * vec4(position.xy, 0.0, 1.0);
}