#version 120
varying vec4 texcoord;
uniform sampler2D texture;

uniform float brightness;
uniform float contrast;
uniform float saturation;
uniform float red;
uniform float green;
uniform float blue;

void performBrightnessChange(float change);
void performContrastChange(float change);
void performSaturationChange(float change);
void performColorChange(float changeRed, float changeBlue, float changeGreen);

void main(void)
{
    gl_FragColor = texture2D(texture, texcoord.xy);

    performBrightnessChange(brightness);
    performContrastChange(contrast);
    performSaturationChange(saturation);
    performColorChange(red, green, blue);
}

void performBrightnessChange(float change)
{
    vec3 new_brightness = gl_FragColor.rgb + max(change, 0.0f);
    gl_FragColor = vec4(new_brightness.xyz, gl_FragColor.a);
}

void performContrastChange(float change)
{
    vec3 new_contrast = ((gl_FragColor.rgb - 0.5f) * max(change, 0.0f)) + 0.5f;
    gl_FragColor = vec4(new_contrast.xyz, gl_FragColor.a);
}

void performSaturationChange(float change)
{
    vec3 new_saturation = clamp(gl_FragColor.rgb - vec3(change) * gl_FragColor.rgb * -1.0, 0.0f, 1.0f);
    gl_FragColor = vec4(new_saturation.xyz, gl_FragColor.a);
}

void performColorChange(float changeRed, float changeBlue, float changeGreen)
{
    vec3 new_color = gl_FragColor.rgb + vec3(changeRed, changeBlue, changeGreen);
    gl_FragColor = vec4(new_color.xyz, gl_FragColor.a);
}
