import { Navigation } from './components/navigation';
import { HeroSection } from './components/sections/hero-section';
import { FeaturesSection } from './components/sections/features-section';
import { UseCaseSection } from './components/sections/use-case-section';
import { Footer } from './components/footer';
import {getServerSession} from "next-auth";

const WelcomePage = async () => {
    const session = await getServerSession();

    return (
        <div className="min-h-screen bg-white">
            <Navigation session={session} />
            <HeroSection session={session} />
            <FeaturesSection />
            <UseCaseSection />
            <Footer />
        </div>
    );
};

export default WelcomePage;