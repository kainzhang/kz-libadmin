package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jfoenix.controls.JFXListView;

import application.Launcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import util.Receiver;

public class clientController extends Thread {
	static DatagramPacket inPacket;
	static InetAddress sAddr;
	static DatagramSocket dSocket = null;
	static int port = 8878;
	
	@FXML
	public ListView  view;
	@FXML
	public TextArea writeField;
	@FXML
	public Button sendBtn;
	@FXML
	public Button sendImageBtn;

	byte[] inBuffer = new byte[500];
	
	
	@FXML
	public void initialize() throws IOException {

		dSocket = new DatagramSocket();
//		sAddr = InetAddress.getByName("172.30.94.118");
		sAddr = InetAddress.getByName("localhost");
		UDPServer a = new UDPServer();
		a.start();
		String s = "User "+Launcher.getUser().getUsername()+" is requesting support.";
		byte[] outBuffer = s.getBytes();
		DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, sAddr, port);
		dSocket.send(outPacket);
		
	}

	//发送数据butten事件
		@SuppressWarnings("unchecked")
		@FXML
		public void sendm() throws IOException {
			
			if (!writeField.getText().equals("")) {
				HBox hb = new HBox();
				hb.getChildren().add(new Text("\n"+writeField.getText()+"\n"));
				hb.setAlignment(Pos.CENTER_RIGHT);
				view.getItems().add(hb);
//				view.getItems().add(new Text(writeField.getText()));
				// 发送数据报到客户端
				if(dSocket==null) {
					//System.out.println("" +dSocket.getLocalPort()+cAddr.getHostAddress()+cPort);
					view.getItems().add(new Text("没有用户链接，发送失败"));
				}
				else {
					byte[] outBuffer = writeField.getText().getBytes();
					DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, sAddr, port);
					// 发送数据报
					dSocket.send(outPacket);
				}

				writeField.setText("");
			}
		}
	
		//接受数据线程
	public class UDPServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			try {
				inPacket = new DatagramPacket(inBuffer, inBuffer.length);
				while (true) {
					dSocket.receive(inPacket);
					String s = new String(inPacket.getData(), 0, inPacket.getLength());
					//如果对面发送文件
					if(s.indexOf("@file@")!=-1) {
						view.getItems().add(new Text("对方发送文件 点击接收"));
						Button bb=new Button("接收");
						bb.setOnAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								// TODO 自动生成的方法存根
								String fileName=s.substring(s.indexOf("@file@")+6);
								byte[] outBuffer = ("@Down@").getBytes();
								DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, sAddr, port);
								// 发送数据报
								try {
									dSocket.send(outPacket);
									update ad=new update(s);
									ad.start();
									
								} catch (IOException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
							}
						});
						view.getItems().add(bb);
					}
					else if(s.indexOf("@滑稽@")!=-1) {
						Image image = new Image("huaji.gif");
						ImageView imageView = new ImageView();
						imageView.setImage(image);
						view.getItems().add(imageView);
					}
					else if(inPacket.getPort()==port){
						HBox hb = new HBox();
						Calendar calendar= Calendar.getInstance();
						SimpleDateFormat dateFormat= new SimpleDateFormat("hh:mm:ss");
						hb.getChildren().add(new Text("\n"+dateFormat.format(calendar.getTime())+" 客服小姐姐: \n\t" + s+"\n"));
						hb.setAlignment(Pos.CENTER_LEFT);
						view.getItems().add(hb);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			view.getItems().add(new Text("服务器断开"));
		}
	}
	
	public class update extends Thread{
		String s="";
		public update(String s) {
			this.s=s;
		}
		public void run() {
			try {
				Receiver.receiveAndCreate(12334,"D:\\123"+s.substring(s.indexOf(".")),dSocket);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
