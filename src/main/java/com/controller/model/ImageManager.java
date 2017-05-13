package com.controller.model;

import java.util.concurrent.LinkedBlockingQueue;

public class ImageManager {
	private static LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();
	
	public static LinkedBlockingQueue<byte[]> getQueue(){
		return queue;
	}
	
}
