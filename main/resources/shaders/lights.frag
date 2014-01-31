 #version 120
 //Texture vars
 uniform sampler2D texture;
 uniform sampler1D lightData;
 uniform sampler1D colorData;
 varying vec4 texcoord;

 //Lighting vars
 uniform float count;
 uniform float overall_brightness;

 //Screen vars
 uniform vec2 cameraPos;
 uniform vec2 window;
 uniform vec2 aspect;
 uniform vec2 iResolution;

 void main() {
     //Get the original pixel color
     gl_FragColor = texture2D(texture, texcoord.xy);
     gl_FragColor.a = min(gl_FragColor.a, gl_Color.a);
     gl_FragColor.rgb *= gl_Color.rgb;

     //Get the pixel location
     vec2 pos = gl_FragCoord.xy / iResolution.xy;

     //Create constants for scaling
     float scalex = 1.0 / (iResolution.x * 0.5);
     float scaley = 1.0 / (iResolution.y * 0.5);
     float ysomething = window.y / iResolution.y;
     scalex *= window.x / iResolution.x;
     scaley *= ysomething;

     for (float i = 0; i < count; i++) {
         //extract data from texture
         float t = (i / count) - 0.1;
         vec3 light = texture1D(lightData, t).rgb;
         vec4 color = texture1D(colorData, t);


         //Translate light's world space to screen space
         vec2 lightPos = vec2(scalex * (light.x - cameraPos.x), ysomething - (scaley * (light.y - cameraPos.y)));

         //Adjust for aspect ratio
         vec2 dis = vec2(abs(pos.x - lightPos.x), abs(pos.y - lightPos.y));
         dis *= (aspect * 0.1);

         //Calculate percent of light
         float xdis = dis.x;
         float ydis = dis.y;
         float magnitude = sqrt((xdis * xdis) + (ydis * ydis));
         float percent = max((light.b - magnitude) / light.b, 0.0);

         color = vec4(max(color.r, gl_FragColor.r), max(color.g, gl_FragColor.g), max(color.b, gl_FragColor.b), color.a);
         gl_FragColor.rgb = (gl_FragColor.rgb * (1.0 - percent)) + (color.rgb * percent);
         gl_FragColor.rgb *= (overall_brightness * (1.0 - percent)) + (color.a * percent);
     }
 }