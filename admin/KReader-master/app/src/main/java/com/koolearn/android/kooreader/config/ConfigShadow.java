package com.koolearn.android.kooreader.config;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.koolearn.android.kooreader.api.KooReaderIntents;
import com.koolearn.klibrary.core.options.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ConfigShadow extends Config implements ServiceConnection {
	private final Context myContext;
	private volatile ConfigInterface myInterface;
	private final List<Runnable> myDeferredActions = new LinkedList<Runnable>();

	private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			try {
				setToCache(
					intent.getStringExtra("group"),
					intent.getStringExtra("name"),
					intent.getStringExtra("value")
				);
			} catch (Exception e) {
				// ignore
			}
		}
	};

	public ConfigShadow(Context context) {
		myContext = context;
		context.bindService(
			KooReaderIntents.internalIntent(KooReaderIntents.Action.CONFIG_SERVICE),
			this,
			Service.BIND_AUTO_CREATE
		);
	}

	@Override
	public boolean isInitialized() {
		return myInterface != null;
	}

	@Override
	public void runOnConnect(Runnable runnable) {
		if (myInterface != null) {
			runnable.run();
		} else {
			synchronized (myDeferredActions) {
				myDeferredActions.add(runnable);
			}
		}
	}

	@Override
	public List<String> listGroups() {
		if (myInterface == null) {
			return Collections.emptyList();
		}
		try {
			return myInterface.listGroups();
		} catch (RemoteException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public List<String> listNames(String group) {
		if (myInterface == null) {
			return Collections.emptyList();
		}
		try {
			return myInterface.listNames(group);
		} catch (RemoteException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public void removeGroup(String name) {
		if (myInterface != null) {
			try {
				myInterface.removeGroup(name);
			} catch (RemoteException e) {
			}
		}
	}

	public boolean getSpecialBooleanValue(String name, boolean defaultValue) {
		return myContext.getSharedPreferences("kooreader.ui", Context.MODE_PRIVATE)
			.getBoolean(name, defaultValue);
	}

	public void setSpecialBooleanValue(String name, boolean value) {
		myContext.getSharedPreferences("kooreader.ui", Context.MODE_PRIVATE).edit()
			.putBoolean(name, value).commit();
	}

	public String getSpecialStringValue(String name, String defaultValue) {
		return myContext.getSharedPreferences("kooreader.ui", Context.MODE_PRIVATE)
			.getString(name, defaultValue);
	}

	public void setSpecialStringValue(String name, String value) {
		myContext.getSharedPreferences("kooreader.ui", Context.MODE_PRIVATE).edit()
			.putString(name, value).commit();
	}

	@Override
	protected String getValueInternal(String group, String name) throws NotAvailableException {
		if (myInterface == null) {
			throw new NotAvailableException("Config is not initialized for " + group + ":" + name);
		}
		try {
			return myInterface.getValue(group, name);
		} catch (RemoteException e) {
			throw new NotAvailableException("RemoteException for " + group + ":" + name);
		}
	}

	@Override
	protected void setValueInternal(String group, String name, String value) {
		if (myInterface != null) {
			try {
				myInterface.setValue(group, name, value);
			} catch (RemoteException e) {
			}
		}
	}

	@Override
	protected void unsetValueInternal(String group, String name) {
		if (myInterface != null) {
			try {
				myInterface.unsetValue(group, name);
			} catch (RemoteException e) {
			}
		}
	}

	@Override
	protected Map<String,String> requestAllValuesForGroupInternal(String group) throws NotAvailableException {
		if (myInterface == null) {
			throw new NotAvailableException("Config is not initialized for " + group);
		}
		try {
			final Map<String,String> values = new HashMap<String,String>();
			for (String pair : myInterface.requestAllValuesForGroup(group)) {
				final String[] split = pair.split("\000");
				switch (split.length) {
					case 1:
						values.put(split[0], "");
						break;
					case 2:
						values.put(split[0], split[1]);
						break;
				}
			}
			return values;
		} catch (RemoteException e) {
			throw new NotAvailableException("RemoteException for " + group);
		}
	}

	// method from ServiceConnection interface
	public void onServiceConnected(ComponentName name, IBinder service) {
		synchronized (this) {
			myInterface = ConfigInterface.Stub.asInterface(service);
			myContext.registerReceiver(
				myReceiver, new IntentFilter(KooReaderIntents.Event.CONFIG_OPTION_CHANGE)
			);
		}

		final List<Runnable> actions;
		synchronized (myDeferredActions) {
			actions = new ArrayList<Runnable>(myDeferredActions);
			myDeferredActions.clear();
		}
		for (Runnable a : actions) {
			a.run();
		}
	}

	// method from ServiceConnection interface
	public synchronized void onServiceDisconnected(ComponentName name) {
		myContext.unregisterReceiver(myReceiver);
	}
}
