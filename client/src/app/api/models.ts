export interface TrackLane { laneNumber: number; color: string; }
export interface TrackConfig { id?: string; name: string; laneCount: number; lanes: TrackLane[]; }

export interface Racer { id?: string; firstName: string; lastName: string; number: string; carName?: string; }

export interface TournamentConfig {
  id?: string;
  name: string;
  trackConfigId: string;
  racerIds: string[];
  algorithmId: string;
  algorithmConfig: Record<string, any>;
  timeoutSeconds: number;
}

export interface AlgoInfo { id: string; name: string; }

export type RunState =
  'READY_FOR_NEXT_LEG' |
  'AWAITING_CLIENT_ACK_START' |
  'AWAITING_START_SIGNAL' |
  'RUNNING_LEG' |
  'LEG_COMPLETE_AWAITING_CLIENT_DECISION' |
  'TOURNAMENT_COMPLETE';

export interface LaneAssignment { laneNumber: number; laneColor: string; racerId: string; }
export interface RaceLeg { legNumber: number; assignments: LaneAssignment[]; }

export interface RacerSummary { racerId: string; displayName: string; laneNumber: number; laneColor: string; }

export interface RacerRanking { racerId: string; rank: number; totalTimeHundredths: number; }

export interface RunView {
  runId: string;
  state: RunState;
  currentLeg?: RaceLeg;
  currentLegRacers: RacerSummary[];
  lastLegResult?: any;
  rankings: RacerRanking[];
}
