package com.yunmel.game.cmd;

public interface ICommand extends Cloneable {
	//执行命令
	public void action();
	//复制命令
	public Object clone() throws CloneNotSupportedException;
}