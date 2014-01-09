/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */


#version 150

out vec4 FragColor;
in  vec3 vNormal;
in  vec3 vPosition;
uniform float focalDistance;

void main(void)
{
if ( abs(-vPosition.z-focalDistance) > 0.025  )
	FragColor = vec4( 0.5 + 0.5 * normalize(vNormal), 1.0 );
else
        FragColor = vec4( 1.0 );
}