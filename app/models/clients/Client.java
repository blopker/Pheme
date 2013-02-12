package models.clients;

import java.util.Collection;

import models.datatypes.DataType;

public interface Client {

	void sendAll(Collection<? extends DataType> datas);

	void send(DataType data);

}
