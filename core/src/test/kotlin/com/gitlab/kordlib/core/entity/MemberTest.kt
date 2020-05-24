package com.gitlab.kordlib.core.entity

import com.gitlab.kordlib.core.behavior.MemberBehavior
import com.gitlab.kordlib.core.cache.data.MemberData
import com.gitlab.kordlib.core.cache.data.UserData
import equality.BehaviorEqualityTest
import equality.GuildEntityEqualityTest
import io.mockk.every
import io.mockk.mockk

internal class MemberTest : GuildEntityEqualityTest<Member> by GuildEntityEqualityTest({ id, guildId ->
    val memberData = mockk<MemberData>()
    every { memberData.userId } returns id.longValue
    every { memberData.guildId } returns guildId.longValue

    val userData = mockk<UserData>()
    every { userData.id } returns id.longValue

    Member(memberData, userData, mockk())
}), BehaviorEqualityTest<Member> {
    override fun Member.behavior(): Entity = MemberBehavior(guildId = guildId, id = id, kord = kord)
}