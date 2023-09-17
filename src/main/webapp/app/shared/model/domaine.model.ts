import { IDocTopic } from '@/shared/model/doc-topic.model';

export interface IDomaine {
  id?: number;
  name?: string | null;
  code?: string | null;
  notes?: string | null;
  topics?: IDocTopic[] | null;
}

export class Domaine implements IDomaine {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public notes?: string | null,
    public topics?: IDocTopic[] | null
  ) {}
}
