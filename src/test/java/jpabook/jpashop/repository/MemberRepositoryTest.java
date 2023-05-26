package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember(){
        Member member = new Member();
        member.setName("memberA");
        Long saveId = memberRepository.save(member);

        Member findMember = memberRepository.findOne(saveId);

        Assertions.assertEquals(member.getId(), findMember.getId());
        Assertions.assertEquals(member.getName(), findMember.getName());
        Assertions.assertEquals(member, findMember);
    }

}