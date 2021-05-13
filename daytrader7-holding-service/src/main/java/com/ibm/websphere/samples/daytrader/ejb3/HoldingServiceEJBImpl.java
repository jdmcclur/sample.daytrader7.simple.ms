/**
 * (C) Copyright IBM Corporation 2015, 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.samples.daytrader.ejb3;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.interfaces.HoldingService;
import com.ibm.websphere.samples.daytrader.util.Log;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HoldingServiceEJBImpl implements HoldingService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private Log Log;

  @Override
  public List<HoldingDataBean> getHoldings(String userID) {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:getHoldings", userID);
    }

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<HoldingDataBean> criteriaQuery = criteriaBuilder.createQuery(HoldingDataBean.class);
    Root<HoldingDataBean> holdings = criteriaQuery.from(HoldingDataBean.class);
    criteriaQuery.where(criteriaBuilder.equal(holdings.get("accountId"),
        criteriaBuilder.parameter(String.class, "p_accountId")));
    criteriaQuery.select(holdings);

    TypedQuery<HoldingDataBean> typedQuery = entityManager.createQuery(criteriaQuery);
    typedQuery.setParameter("p_accountId", userID);

    return typedQuery.getResultList();
  }

  @Override
  public HoldingDataBean getHolding(Integer holdingID) {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:getHolding", holdingID);
    }
    return entityManager.find(HoldingDataBean.class, holdingID);
  }

  @Override
  public HoldingDataBean createHolding(String accountId, String quoteSymbol, double quantity, BigDecimal purchasePrice)
      throws Exception {
    HoldingDataBean newHolding = new HoldingDataBean(quantity, purchasePrice, new Timestamp(System.currentTimeMillis()),
        accountId, quoteSymbol);
    entityManager.persist(newHolding);
    return newHolding;
  }

  @Override
  public void removeHolding(Integer holdingID) {
    if (Log.doTrace()) {
      Log.trace("TradeSLSBBean:getHolding", holdingID);
    }
    HoldingDataBean holding = entityManager.find(HoldingDataBean.class, holdingID);
    entityManager.remove(holding);
  }

}