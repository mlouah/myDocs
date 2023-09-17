import { IDocCollection } from '@/shared/model/doc-collection.model';

export interface IDocPublisher {
  id?: number;
  name?: string | null;
  notes?: string | null;
  collections?: IDocCollection[] | null;
}

export class DocPublisher implements IDocPublisher {
  constructor(
    public id?: number,
    public name?: string | null,
    public notes?: string | null,
    public collections?: IDocCollection[] | null
  ) {}
}
