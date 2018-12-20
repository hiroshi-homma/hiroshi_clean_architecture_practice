package hiroshi_homma.homma_create.hiroshi_create_android.core.functional

import hiroshi_homma.homma_create.hiroshi_create_android.UnitTest
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Left
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class EitherTest : UnitTest() {

    @Test fun `Either Right should return correct type`() {
        val result = Right("ironman")

        result shouldBeInstanceOf Either::class.java
        result.isRight shouldBe true
        result.isLeft shouldBe false
        result.either({},
                { right ->
                    right shouldBeInstanceOf String::class.java
                    right shouldBeEqualTo "ironman"
                })
    }

    @Test fun `Either Left should return correct type`() {
        val result = Left("ironman")

        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldBe true
        result.isRight shouldBe false
        result.either(
                { left ->
                    left shouldBeInstanceOf String::class.java
                    left shouldBeEqualTo "ironman"
                }, {})
    }
}