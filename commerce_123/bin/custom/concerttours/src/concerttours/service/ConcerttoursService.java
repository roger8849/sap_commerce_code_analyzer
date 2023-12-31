/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package concerttours.service;

public interface ConcerttoursService {

  String getHybrisLogoUrl(String logoCode);

  void createLogo(String logoCode);
}
