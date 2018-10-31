package com.haiyu;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Title: NettyServerHandler
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/31 17:09
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf file = (ByteBuf) msg;

        //3.1 读取文件名
        byte[] fileNameBuf = new byte[128];
        file.readBytes(fileNameBuf);

        String fileName = null;
        try {
            fileName = new String(fileNameBuf,"utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("filename:" + fileName);

        //3.2保存字节流文件到磁盘
        ByteBuffer buffer = file.nioBuffer();

        FileOutputStream targetFileOutputStream = null;
        FileChannel targetFileChannel = null;
        try {
            try {
                targetFileOutputStream = new FileOutputStream(new File(fileName), false);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            targetFileChannel = targetFileOutputStream.getChannel();
            targetFileChannel.write(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != targetFileOutputStream) {
                try {
                    targetFileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != targetFileChannel) {
                try {
                    targetFileChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != file) {
                file.release();
            }
        }

        ByteBuf message = null;
        String result = fileName + " save ok";
        message = Unpooled.buffer(result.length());
        message.writeBytes(result.getBytes());

        ctx.writeAndFlush(message);//.addListener(ChannelFutureListener.CLOSE);
        System.out.println("save file ok");

    }
}
