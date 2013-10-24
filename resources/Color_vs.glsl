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
#extension GL_ARB_explicit_attrib_location : enable


layout(location=0) in vec3 aPosition;


uniform mat4 uModel;
uniform mat4 uProjection;


void main(void) 
{
	gl_Position = uProjection * uModel * vec4(aPosition, 1.0);
}