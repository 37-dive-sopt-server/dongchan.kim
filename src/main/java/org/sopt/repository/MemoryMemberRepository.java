package org.sopt.repository;

import org.sopt.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {


    private static final Map<Long, Member> store = new HashMap<>();


    public Member save(Member member) {
        store.put(member.getId(), member);
        return member;
    }


    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }


    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}
