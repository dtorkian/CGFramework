package m.blatt4;

/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import math.Mat3;
import math.Mat4;
import math.Vec3;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import util.Color;
import util.Mesh;
import util.ShaderProgram;

public class Sandbox {
	private ShaderProgram shaderProgram;
	private Mesh triangleMesh;
	private Mat4 modelMatrix;
	private Mat4 viewMatrix;
	private Mat4 projectionMatrix;
	private boolean oPresp = false;
	private int fovy =60;

	/**
	 * @param width
	 *            The horizontal window size in pixels
	 * @param height
	 *            The vertical window size in pixels
	 */
	public Sandbox(int width, int height) {
		shaderProgram = new ShaderProgram("Color_vs.glsl", "Color_fs.glsl");

		modelMatrix = new Mat4();
		viewMatrix = Mat4.translation(0.0f, 0.0f, -3.0f);
		triangleMesh = Sphere.createMesh(.25f, 30, 30);

		glEnable(GL_DEPTH_TEST);
	}

	/**
	 * @param deltaTime
	 *            The time in seconds between the last two frames
	 */
	public void update(float deltaTime) {
		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
			fovy++;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
			fovy--;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			viewMatrix.mul(Mat4.translation(-0.05f, 0.0f, 0.0f));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			viewMatrix.mul(Mat4.translation(0.05f, 0.0f, 0.0f));
			
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Main.exit = true;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_P))
			oPresp = !oPresp;

		float cameraSpeed = 5.0f * deltaTime;

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			viewMatrix.mul(Mat4.translation(0.0f, 0.0f, cameraSpeed));

		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			viewMatrix.mul(Mat4.translation(0.0f, 0.0f, -cameraSpeed));

		if (Mouse.isButtonDown(0)) {
			float rotationScale = 0.01f;

			float deltaX = (float) Mouse.getDX();
			float deltaY = (float) Mouse.getDY();

			Mat4 rotationX = Mat4
					.rotation(Vec3.yAxis(), deltaX * rotationScale);
			Mat4 rotationY = Mat4.rotation(Vec3.xAxis(), -deltaY
					* rotationScale);

			viewMatrix = rotationY.mul(rotationX).mul(viewMatrix);
		}
	}

	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if (!oPresp)
			projectionMatrix = Mat4.perspective(fovy, 1280f,
				720f, 0.01f, 500.0f);
		else
		projectionMatrix = Mat4.orthographic(-3, 3, 1.75f, -1.75f, 0, 300);
		

		shaderProgram.useProgram();
		for(int i=0; i<8 ; i++){
			for (int j=0; j<6; j++){
				drawKugel(0+i*.7f,0-j*.55f);
			}
				
		}
	

	}

	private void drawKugel(float x, float y) {
		modelMatrix = Mat4.translation(-2.5f+x, 1.4f+y, 0);
		shaderProgram.setUniform("uModel", modelMatrix);
		shaderProgram.setUniform("uView", viewMatrix);
		shaderProgram.setUniform("uProjection", projectionMatrix);
		shaderProgram.setUniform("uColor", new Vec3(.3f+x/7,1f-x/7,0+y/-7));

		triangleMesh.draw(GL_TRIANGLES);

	}

}
