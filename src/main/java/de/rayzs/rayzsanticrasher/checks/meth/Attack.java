package de.rayzs.rayzsanticrasher.checks.meth;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import de.rayzs.rayzsanticrasher.runtime.RuntimeExec;

public class Attack {

	private String taskName;
	private List<String> whiteList, blackList, waitingList;
	private HashMap<String, Integer> connectionHash;
	private Integer totalConnections, task;
	private Boolean isRunning, underAttack, useIPTable;

	public Attack(String taskName, Boolean useIPTable) {
		this.taskName = taskName;
		underAttack = false;
		this.useIPTable = useIPTable;
		whiteList = new LinkedList<>();
		blackList = new LinkedList<>();
		waitingList = new LinkedList<>();
		connectionHash = new LinkedHashMap<>();
		totalConnections = 0;
		isRunning = true;
		start();
	}

	public void start() {
		startScheduler();
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(task);
	}

	public void setState(Boolean bool) {
		underAttack = bool;
	}

	public void addConnection() {
		totalConnections++;
	}

	public void addWhitelist(String address) {
		if (isWhitelisted(address))
			return;
		whiteList.add(address);
	}
	
	public void setWhitelist(List<String> list) {
		whiteList = list;
	}

	public void addBlacklist(String address) {
		if (isBlacklisted(address))
			return;
		blackList.add(address);
	}

	public void addWaiting(String address) {
		if (isWaiting(address))
			return;
		waitingList.add(address);
	}

	public void removeWhitelist(String address) {
		if (!isWhitelisted(address))
			return;
		whiteList.remove(address);
	}

	public void removeBlacklist(String address) {
		if (!isBlacklisted(address))
			return;
		blackList.remove(address);
	}

	public void removeWaiting(String address) {
		if (!isWaiting(address))
			return;
		waitingList.remove(address);
	}

	public void clearWhitelist() {
		whiteList.clear();
	}

	public void clearBlacklist() {
		blackList.clear();
	}

	public void clearWaitinglist() {
		waitingList.clear();
	}

	public void addConnection(String address) {
		setConnection(address, (getConnections(address)) + (1));
	}

	public void setConnection(String address, Integer amount) {
		connectionHash.put(address, amount);
	}

	public List<String> getWhitelist() {
		return this.whiteList;
	}

	public List<String> getBlacklist() {
		return this.blackList;
	}

	public List<String> getWaitinglist() {
		return this.waitingList;
	}

	public String getTaskName() {
		return taskName;
	}

	public Integer getWhitelistSize() {
		return this.whiteList.size();
	}

	public Integer getBlacklistSize() {
		return this.blackList.size();
	}

	public Integer getWaitinglistSize() {
		return this.waitingList.size();
	}

	public String getPacketName() {
		return taskName;
	}

	public Boolean isUnderAttack() {
		return underAttack;
	}

	public Boolean isWhitelisted(String address) {
		return getWhitelist().contains(address);
	}

	public Boolean isBlacklisted(String address) {
		return getBlacklist().contains(address);
	}

	public Boolean isWaiting(String address) {
		return getWaitinglist().contains(address);
	}

	public Boolean hasConnection(String address) {
		try {
			return (connectionHash.get(address) != null);
		} catch (Exception error) {
			return false;
		}
	}

	public Integer getConnections() {
		return totalConnections;
	}

	public Integer getConnections(String address) {
		if (hasConnection(address))
			return connectionHash.get(address);
		setConnection(address, 0);
		return 0;
	}

	public void ipTable(String address, Boolean bool) {
		if (!useIPTable)
			return;
		if (bool) {
			new RuntimeExec("iptables -I INPUT -s " + address + " -j DROP");
			return;
		}
		new RuntimeExec("iptables -D INPUT -s " + address + " -j DROP");
	}

	private void startScheduler() {
		(new Thread(() -> {
			while (isRunning) {
				totalConnections = 0;
				connectionHash = new HashMap<>();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException error) {
				}
			}
		})).start();
	}
}