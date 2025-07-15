package kr.co.sist.e_learning.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDAOImpl implements SubscriptionDAO {

    private final String NS = "kr.co.sist.e_learning.mypage.SubscriptionMapper";

    @Autowired
    private SqlSession session;

    @Override
    public List<SubscriptionDTO> selectSubscriptions(long userSeq) {
        return session.selectList(NS + ".selectSubscriptions", userSeq);
    }

    @Override
    public void deleteSubscription(Map<String, Object> map) {
        session.delete(NS + ".deleteSubscription", map);
    }
}
