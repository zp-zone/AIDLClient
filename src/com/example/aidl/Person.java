package com.example.aidl;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 
 * @Description	User defined type should implements Parcelable interface and 
 * 				have a static final Parcelable.Creator<T> CREATOR var,
 * 				writeToParcel() should be overwrite
 * 
 * @Date		2015-10-23 下午10:00:43
 * @Author		SugarZ
 */
public class Person implements Parcelable {

	private int age;
	private String name;
	private float scores[];
	public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {

		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}

		@Override
		public Person createFromParcel(Parcel source) {
			return new Person(source);
		}
	};

	public Person() {
		super();
		this.age = 20;
		this.name = "zhangsan";
		this.scores = new float[] {60,70,80};
	}

	public Person(int age, String name, float[] scores) {
		super();
		this.age = age;
		this.name = name;
		this.scores = scores;
	}

	public Person(Parcel source) {
		age = source.readInt();
		name = source.readString();
		int len = source.readInt();
		if (len > 0) {
			this.scores = new float[len];
			source.readFloatArray(this.scores);
		}
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float[] getScores() {
		return scores;
	}

	public void setScores(float[] scores) {
		this.scores = scores;
	}

	public static Parcelable.Creator<Person> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(age);
		dest.writeString(name);
		if (scores != null) {
			dest.writeInt(scores.length);
			dest.writeFloatArray(scores);
		} else {
			dest.writeInt(0);
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
